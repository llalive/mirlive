package tv.mir24.mirlive;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String[] descriptions =
            {"Телеканал «Мир» знакомит зрителей с современной жизнью и историей стран бывшего Советского Союза, формирует культурные, социальные и экономические связи. Основу контента телеканала составляют информационно-аналитические, познавательные, развлекательные и публицистические программы, в том числе и для детей. Значительная часть эфира отведена художественным фильмам и сериалам.",
             "Телеканал «Мир 24» — информационный, культурологический и страноведческий телеканал, который начал вещание 1 января 2013 года в 6:00 МСК, рассказывает о новостях в странах СНГ и в мире. Каждые полчаса в прямом эфире выходят 15-минутные выпуски оперативной информации, каждые 4 часа по будням — 25-минутные блоки новостей с репортажами, прямыми включениями и комментариями. Круглосуточно — информационные ленты в виде бегущей строки с «горящими» новостями и темами дня.",
             "«Мир HD» — это развлекательно-познавательный телеканал высокой чёткости. Канал предлагает своим зрителям большое количество художественных фильмов и сериалов. В эфире «Мир HD» есть линейки западного и российского кино. Широко представлены программы о путешествиях, красоте и здоровье, ток-шоу, информационные и публицистические передачи, а также проекты для детей.",
             "Радиостанция «Мир» вещает в музыкально-информационном формате. Каждые полчаса в эфире — эксклюзивные новости от собственных корреспондентов в странах СНГ и российских городах вещания. Основа музыкальной политики — песни 1990—2000-х годов на русском языке. Представлены также образовательные, литературно-художественные программы, ток-шоу, радиомосты со странами СНГ.",
             "Телеканал «Мир» знакомит зрителей с современной жизнью и историей стран бывшего Советского Союза, формирует культурные, социальные и экономические связи. Основу контента телеканала составляют информационно-аналитические, познавательные, развлекательные и публицистические программы, в том числе и для детей. Значительная часть эфира отведена художественным фильмам и сериалам."};
    static final String[] logos =
            {"mirtv_logo", "mir24_logo", "mirhd_logo", "radio_logo", "mirtv_logo"};

    int pageNumber;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("Info", "channel onCreate " + pageNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("Info", "channel onCreateView " + pageNumber);
        View view = inflater.inflate(R.layout.channel, null);

        TextView  channelDesc = (TextView)  view.findViewById(R.id.channelDesc);
        ImageView       thumb = (ImageView) view.findViewById(R.id.inTextLogo);
        Drawable channelLogo = ContextCompat.getDrawable(view.getContext(),
                getResources().getIdentifier(logos[pageNumber], "drawable", getActivity().getPackageName()));
        thumb.setImageDrawable(channelLogo);

        FlowTextHelper.tryFlowText(descriptions[pageNumber], thumb, channelDesc,
                getActivity().getWindowManager().getDefaultDisplay());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("Info", "channel onDestroyView " + pageNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        Log.d("Info", "channel onDestroy " + pageNumber);
    }

}
